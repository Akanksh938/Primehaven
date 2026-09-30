$ports = @(8761, 8080, 8081, 8082, 8083, 5173)
foreach ($port in $ports) {
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

Write-Host 'PrimeHaven processes stopped.'
