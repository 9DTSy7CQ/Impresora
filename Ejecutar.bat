@echo off

echo === Matando procesos Java anteriores ===
taskkill /F /IM java.exe >nul 2>&1
timeout /t 1 >nul

echo === Compilando proyecto ===
if not exist target\classes mkdir target\classes

javac -d target/classes ^
  src/main/java/es/etg/dam/util/HashUtil.java ^
  src/main/java/es/etg/dam/util/CifradoUtil.java ^
  src/main/java/es/etg/dam/util/RegistroImpresion.java ^
  src/main/java/es/etg/dam/util/LogUtil.java ^
  src/main/java/es/etg/dam/exception/HashNoCoincideException.java ^
  src/main/java/es/etg/dam/exception/GestionClienteException.java ^
  src/main/java/es/etg/dam/exception/ImpresoraException.java ^
  src/main/java/es/etg/dam/exception/ClienteException.java ^
  src/main/java/es/etg/dam/exception/ServidorException.java ^
  src/main/java/es/etg/dam/Conexion.java ^
  src/main/java/es/etg/dam/Tinta.java ^
  src/main/java/es/etg/dam/Impresora.java ^
  src/main/java/es/etg/dam/ClienteHandler.java ^
  src/main/java/es/etg/dam/servidor/Servidor.java ^
  src/main/java/es/etg/dam/cliente/Cliente.java

if %errorlevel% neq 0 (
    echo Error al compilar. Revisa el codigo.
    pause
    exit /b 1
)
echo Compilacion OK

echo.
echo === Iniciando servidor en segundo plano ===
start "Servidor Impresora" java -cp target/classes es.etg.dam.servidor.Servidor

echo Esperando a que arranque el servidor...
timeout /t 2 >nul

echo.
echo === Lanzando clientes ===
java -cp target/classes es.etg.dam.cliente.Cliente "BN 5"
java -cp target/classes es.etg.dam.cliente.Cliente "COLOR 3"
java -cp target/classes es.etg.dam.cliente.Cliente "BN 10"
java -cp target/classes es.etg.dam.cliente.Cliente "COLOR 25"

echo.
echo === Registro generado ===
if exist impresora.log (
    type impresora.log
) else (
    echo No se genero impresora.log
)

echo.
pause