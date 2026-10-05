# Starts the whole JobNova backend locally (build first with: mvn -DskipTests install)
# Requires: PostgreSQL on localhost:5432 with the databases from init-db.sql.
# Logs go to .\logs\<service>.log ; stop everything with: Get-Process java | Stop-Process
$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
New-Item -ItemType Directory -Force "$root\logs" | Out-Null

# Load .env into this process so every service inherits it (JWT_SECRET, GEMINI_API_KEY, ...)
Get-Content "$root\.env" | Where-Object { $_ -match '^\s*[^#\s][^=]*=' } | ForEach-Object {
    $k, $v = $_ -split '=', 2
    [Environment]::SetEnvironmentVariable($k.Trim(), $v.Trim().Trim('"').Trim("'"), "Process")
}

function Start-JobNova($module, $name, $extra = @()) {
    $jar = Get-ChildItem "$root\$module\target\*-SNAPSHOT.jar" | Select-Object -First 1
    Start-Process java -ArgumentList (@("-Xmx384m", "-jar", "`"$($jar.FullName)`"") + $extra) `
        -WorkingDirectory "$root\$module" -WindowStyle Hidden `
        -RedirectStandardOutput "$root\logs\$name.log" -RedirectStandardError "$root\logs\$name.err.log"
    Write-Host "started $name"
}

function Wait-Url($url) {
    for ($i = 0; $i -lt 90; $i++) {
        try { Invoke-WebRequest $url -UseBasicParsing -TimeoutSec 2 | Out-Null; return } catch { Start-Sleep 2 }
    }
    throw "Timed out waiting for $url"
}

Start-JobNova "cloud\Service-Registry" "registry"
Wait-Url "http://localhost:8761/"
Start-JobNova "cloud\Config-Server" "config" @("--eureka.client.service-url.defaultZone=http://localhost:8761/eureka/")
Wait-Url "http://localhost:8888/API-Gateway/default"

Start-JobNova "services\User-Services" "user"
Start-JobNova "services\Company-Services" "company"
Start-JobNova "services\Job-Service" "job"
Start-JobNova "services\Resume-Service" "resume"
Start-JobNova "services\Application-Service" "application"
Start-JobNova "services\Job-Preferences" "preferences"
Start-JobNova "services\AI-Service" "ai"
# Notification-Service needs Kafka (docker\docker-compose.dev.yml); start it separately if Kafka is running
Start-JobNova "cloud\API-Gateway" "gateway"

Write-Host "JobNova API Gateway: http://localhost:5000  (services need ~30s more to register with Eureka)"
