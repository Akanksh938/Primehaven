$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendRoot = Join-Path $projectRoot 'backend'
$frontendRoot = Join-Path $projectRoot 'frontend'
$mavenBin = 'C:\Users\User\.maven\maven-3.9.15\bin'

if (-not (Test-Path $mavenBin)) {
    throw "Maven not found at $mavenBin. Update the path in this script first."
}

$env:PATH = "$mavenBin;$env:PATH"

# Keep database credentials out of source control. The value is inherited only
# by the Java processes started from this script and is never written to disk.
if ([string]::IsNullOrWhiteSpace($env:PRIMEHAVEN_DB_PASSWORD)) {
    $securePassword = Read-Host 'Enter MySQL password (press Enter if none)' -AsSecureString
    $passwordPointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    try {
        $env:PRIMEHAVEN_DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPointer)
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPointer)
    }
}

function Stop-PortProcess {
    param([int[]]$Ports)
    foreach ($port in $Ports) {
        $connections = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
        if ($connections) {
            foreach ($connection in $connections) {
                try {
                    Stop-Process -Id $connection.OwningProcess -Force -ErrorAction SilentlyContinue
                }
                catch {
                    Write-Host "Could not stop process on port $port."
                }
            }
        }
    }
}

function Wait-Url {
    param(
        [string]$Url,
        [int]$TimeoutSeconds = 90
    )

    $deadline = [DateTime]::UtcNow.AddSeconds($TimeoutSeconds)
    while ([DateTime]::UtcNow -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 3
            if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 500) {
                return $true
            }
        }
        catch {
            Start-Sleep -Seconds 2
            continue
        }

        Start-Sleep -Seconds 2
    }

    return $false
}

Write-Host 'Stopping any existing app processes on the local ports...'
Stop-PortProcess -Ports @(8761, 8080, 8081, 8082, 8083, 5173)

Write-Host 'Building backend services with Maven...'
Push-Location $backendRoot
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Pop-Location
    throw 'Backend build failed.'
}
Pop-Location

$startupPlan = @(
    @{ Name = 'Eureka'; Jar = Join-Path $backendRoot 'eureka-server\target\eureka-server-0.0.1-SNAPSHOT.jar'; Url = 'http://localhost:8761' },
    @{ Name = 'Property'; Jar = Join-Path $backendRoot 'property-service\target\property-service-0.0.1-SNAPSHOT.jar'; Url = 'http://localhost:8081/actuator/health' },
    @{ Name = 'Visit'; Jar = Join-Path $backendRoot 'visit-service\target\visit-service-0.0.1-SNAPSHOT.jar'; Url = 'http://localhost:8082/actuator/health' },
    @{ Name = 'Inquiry'; Jar = Join-Path $backendRoot 'inquiry-service\target\inquiry-service-0.0.1-SNAPSHOT.jar'; Url = 'http://localhost:8083/actuator/health' },
    @{ Name = 'Gateway'; Jar = Join-Path $backendRoot 'api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar'; Url = 'http://localhost:8080/api/properties' }
)

foreach ($entry in $startupPlan) {
    Write-Host "Starting $($entry.Name) ..."
    Start-Process java -ArgumentList @('-jar', $entry.Jar) -WorkingDirectory $backendRoot -NoNewWindow | Out-Null

    if (-not (Wait-Url -Url $entry.Url -TimeoutSeconds 60)) {
        throw "Timed out waiting for $($entry.Name) at $($entry.Url)"
    }
}

Write-Host 'Starting frontend dev server...'
Start-Process npm.cmd -ArgumentList @('run', 'dev', '--', '--host', '0.0.0.0') -WorkingDirectory $frontendRoot -NoNewWindow | Out-Null

if (-not (Wait-Url -Url 'http://localhost:5173' -TimeoutSeconds 60)) {
    throw 'Timed out waiting for the frontend server on http://localhost:5173'
}

Write-Host ''
Write-Host 'PrimeHaven is running.'
Write-Host 'Frontend: http://localhost:5173'
Write-Host 'Eureka: http://localhost:8761'
Write-Host 'Gateway: http://localhost:8080/api/properties'
