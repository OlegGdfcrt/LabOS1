#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>
#include "employee.h"

using namespace std;

int main(int argc, char* argv[])
{
    setlocale(LC_ALL, "Rus");

    if (argc != 4)
    {
        cout << "Использование: Reporter исходный_файл файл_отчета оплата_за_час" << endl;
        return 1;
    }

    string inputFile = argv[1];
    string outputFile = argv[2];
    double hourlyRate = stod(argv[3]);

    ifstream inFile(inputFile, ios::binary);
    if (!inFile)
    {
        cout << "Ошибка открытия исходного файла" << endl;
        return 1;
    }

    ofstream outFile(outputFile);
    if (!outFile)
    {
        cout << "Ошибка создания файла отчета" << endl;
        return 1;
    }

    outFile << "Отчет по файлу \"" << inputFile << "\"" << endl;
    outFile << "Номер сотрудника, имя сотрудника, часы, зарплата" << endl;

    employee emp;
    while (inFile.read(reinterpret_cast<char*>(&emp), sizeof(employee)))
    {
        double salary = emp.hours * hourlyRate;
        outFile << emp.num << " " << emp.name << " " << emp.hours << " " << salary << endl;
    }

    inFile.close();
    outFile.close();

    cout << "Текстовый отчет создан: " << outputFile << endl;

    return 0;
}