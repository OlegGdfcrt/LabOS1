#include <iostream>
#include <fstream>
#include <string>
#include <windows.h>
#include "employee.h"

using namespace std;

int main()
{
    setlocale(LC_ALL, "Rus");

    string binaryFileName, reportFileName;
    int recordCount;
    double hourlyRate;

    cout << "Введите имя бинарного файла: ";
    cin >> binaryFileName;
    cout << "Введите количество записей: ";
    cin >> recordCount;

    string creatorCmd = "Creator.exe " + binaryFileName + " " + to_string(recordCount);

    STARTUPINFO siCreator;
    PROCESS_INFORMATION piCreator;

    ZeroMemory(&siCreator, sizeof(siCreator));
    siCreator.cb = sizeof(siCreator);
    ZeroMemory(&piCreator, sizeof(piCreator));

    if (!CreateProcess(NULL, (LPSTR)creatorCmd.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &siCreator, &piCreator))
    {
        cout << "Ошибка запуска утилиты Creator" << endl;
        return 1;
    }

    WaitForSingleObject(piCreator.hProcess, INFINITE);
    CloseHandle(piCreator.hProcess);
    CloseHandle(piCreator.hThread);

    ifstream file(binaryFileName, ios::binary);
    if (file)
    {
        cout << "Содержимое бинарного файла '" << binaryFileName << "':" << endl;
        employee emp;
        while (file.read(reinterpret_cast<char*>(&emp), sizeof(employee)))
        {
            cout << "Номер: " << emp.num << ", Имя: " << emp.name << ", Часы: " << emp.hours << endl;
        }
        file.close();
    }

    cout << "Введите имя файла отчета: ";
    cin >> reportFileName;
    cout << "Введите оплату за час работы: ";
    cin >> hourlyRate;

    string reporterCmd = "Reporter.exe " + binaryFileName + " " + reportFileName + " " + to_string(hourlyRate);

    STARTUPINFO siReporter;
    PROCESS_INFORMATION piReporter;

    ZeroMemory(&siReporter, sizeof(siReporter));
    siReporter.cb = sizeof(siReporter);
    ZeroMemory(&piReporter, sizeof(piReporter));

    if (!CreateProcess(NULL, (LPSTR)reporterCmd.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &siReporter, &piReporter))
    {
        cout << "Ошибка запуска утилиты Reporter" << endl;
        return 1;
    }

    WaitForSingleObject(piReporter.hProcess, INFINITE);
    CloseHandle(piReporter.hProcess);
    CloseHandle(piReporter.hThread);

    ifstream reportFile(reportFileName);
    if (reportFile)
    {
        cout << "Содержимое отчета '" << reportFileName << "':" << endl;
        string line;
        while (getline(reportFile, line))
        {
            cout << line << endl;
        }
        reportFile.close();
    }

    cout << "Работа программы завершена." << endl;
    return 0;
}