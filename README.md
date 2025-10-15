# 💫 Project "Library"

<div align="center">

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)
![Multithreading](https://img.shields.io/badge/Multithreading-✓-blue?style=for-the-badge)
![GitHub](https://img.shields.io/badge/GitHub-Packages-lightgrey?style=for-the-badge&logo=github)

**Сервис для управления коллекциями книг и читателей с расширенными возможностями сортировки и поиска**

</div>

## 📖 Описание

Проект представляет собой Java-приложение для работы с коллекциями книг и читателей. Реализованы алгоритмы сортировки и поиска с использованием многопоточности для оптимальной производительности.

## 🚀 Возможности

### 📥 Заполнение коллекций
- **Ручной ввод** данных через интуитивный интерфейс
- **Импорт из JSON** файлов
- **Генерация случайных данных** для тестирования

### ⚡ Алгоритмы сортировки
- **Быстрая сортировка** (Quick Sort)
- **Сортировка слиянием** (Merge Sort)  
- **Гибридная сортировка** с оптимизацией производительности
- *Многопоточная реализация* для максимальной скорости

### 🔍 Поиск и анализ
- **Бинарный поиск** по отсортированным коллекциям
- **Подсчет вхождений** с использованием многопоточности

### 💾 Сохранение результатов
- **Экспорт в JSON** на различных этапах обработки

## 📦 Установка

### Скачать приложение

<div align="center">

[📦 **Download Latest Release**](https://github.com/IPaleLuna/Dream-Team-Project/packages)

</div>

Перейдите в раздел **Packages** нашего репозитория и скачайте готовую сборку приложения.

### Сборка из исходного кода

```bash
# Клонирование репозитория
git clone https://github.com/IPaleLuna/Dream-Team-Project.git
cd Dream-Team-Project

# Компиляция проекта
javac -d bin src/**/*.java

# Запуск приложения
java -cp bin Main
