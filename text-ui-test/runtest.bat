@echo off
setlocal

REM Compile the program
cd ..
call compile.bat
if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)
cd text-ui-test

REM Run test
set TEST_NAME=SampleTest
java -cp ../classes Cherish < input/%TEST_NAME%.txt > actual/%TEST_NAME%.txt

REM Compare outputs
fc expected/%TEST_NAME%.txt actual/%TEST_NAME%.txt > nul
if errorlevel 1 (
    echo.
    echo TEST FAILED!
    echo Differences found between expected and actual output.
    echo Run: fc expected/%TEST_NAME%.txt actual/%TEST_NAME%.txt
) else (
    echo.
    echo ALL TESTS PASSED!
)

pause