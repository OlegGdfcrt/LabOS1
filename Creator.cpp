#include <iostream>
#include <fstream>
#include <string>
#include "employee.h"

using namespace std;

int main(int argc, char* argv[])
{
    setlocale(LC_ALL, "Rus");
    
    if (argc != 3)
    {
        cout << "Нужно: Creator имя_файла количество" << endl;
        return 1;
    }

    string filename = argv[1];
    int count = stoi(argv[2]);

    ofstream file(filename, ios::binary);
    if (!file)
    {
        cout << "Ошибка создания файла" << endl;
        return 1;
    }

    cout << "Введите данные " << count << " сотрудников:" << endl;

    for (int i = 0; i < count; i++)
    {
        employee e;

        cout << "Сотрудник " << i + 1 << ":" << endl;
        cout << "Номер: ";
        cin >> e.num;
        cout << "Имя: ";
        cin >> e.name;
        cout << "Часы: ";
        cin >> e.hours;

        file.write((char*)&e, sizeof(employee));
    }

    file.close();
    cout << "Файл создан: " << filename << endl;

    return 0;
}