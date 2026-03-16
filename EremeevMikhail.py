import math

radius = 7
area = math.pi * radius ** 2
circumference = 2 * math.pi * radius

print(f"Радиус круга: {radius}")
print(f"Площадь круга: {round(area, 2)}")
print(f"Длина окружности: {round(circumference, 2)}")
print(f"Проверка: {radius}² * π ≈ {round(radius**2 * 3.14, 2)}")

print(f"Число Пи: {math.pi}") # Первый коммит в мастера, вывод числа пи
