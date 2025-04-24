#!/bin/bash

# Цвета для вывода
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${CYAN}=== Подготовка проекта для загрузки на GitHub ===${NC}"

# Проверка наличия git
if ! command -v git &> /dev/null; then
    echo -e "${RED}Git не установлен. Пожалуйста, установите Git.${NC}"
    exit 1
fi

# Проверка, инициализирован ли Git-репозиторий
if [ ! -d .git ]; then
    echo -e "${YELLOW}Инициализация Git-репозитория...${NC}"
    git init
    echo -e "${GREEN}✓ Git-репозиторий инициализирован${NC}"
else
    echo -e "${GREEN}✓ Git-репозиторий уже инициализирован${NC}"
fi

# Проверка .gitignore
if [ ! -f .gitignore ]; then
    echo -e "${YELLOW}Создание файла .gitignore...${NC}"
    cat > .gitignore << 'EOL'
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# Java
*.class
*.log
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar
hs_err_pid*
replay_pid*

# IDE files
.idea/
*.iml
*.iws
*.ipr
.settings/
.project
.classpath
.factorypath
.vscode/

# OS specific
.DS_Store
Thumbs.db
*.swp
*~
EOL
    echo -e "${GREEN}✓ Файл .gitignore создан${NC}"
else
    echo -e "${GREEN}✓ Файл .gitignore уже существует${NC}"
fi

# Добавление всех файлов в индекс
echo -e "${YELLOW}Добавление файлов в индекс...${NC}"
git add .
echo -e "${GREEN}✓ Файлы добавлены в индекс${NC}"

# Создание коммита
echo -e "${YELLOW}Создание коммита...${NC}"
git commit -m "Начальная версия системы аренды автомобилей"
echo -e "${GREEN}✓ Коммит создан${NC}"

# Проверка удаленного репозитория
REMOTE_EXISTS=$(git remote -v | grep origin | wc -l)
if [ $REMOTE_EXISTS -eq 0 ]; then
    echo -e "${YELLOW}Добавление удаленного репозитория...${NC}"
    git remote add origin https://github.com/Taulan23/labjava.git
    echo -e "${GREEN}✓ Удаленный репозиторий добавлен${NC}"
else
    echo -e "${GREEN}✓ Удаленный репозиторий уже настроен${NC}"
fi

# Переименование ветки в main
echo -e "${YELLOW}Переименование ветки в main...${NC}"
git branch -M main
echo -e "${GREEN}✓ Ветка переименована в main${NC}"

echo -e "\n${CYAN}=== Проект готов к загрузке на GitHub ===${NC}"
echo -e "${YELLOW}Для завершения загрузки выполните один из следующих вариантов:${NC}"

echo -e "\n${CYAN}Вариант 1: Использовать GitHub Desktop (рекомендуется)${NC}"
echo -e "1. Установите GitHub Desktop с сайта: https://desktop.github.com/"
echo -e "2. Откройте GitHub Desktop и войдите в свою учетную запись"
echo -e "3. Добавьте локальный репозиторий: File > Add Local Repository"
echo -e "4. Найдите папку $(pwd)"
echo -e "5. Нажмите кнопку 'Publish repository'"
echo -e "6. Выберите ваш репозиторий Taulan23/labjava и нажмите 'Publish'"

echo -e "\n${CYAN}Вариант 2: Использовать HTTPS с токеном${NC}"
echo -e "1. Создайте персональный токен на странице: https://github.com/settings/tokens"
echo -e "2. Выполните команду: git push -u origin main"
echo -e "3. Когда запросит имя пользователя, введите ваш логин GitHub"
echo -e "4. Когда запросит пароль, введите ваш персональный токен (не пароль от GitHub)"

echo -e "\n${CYAN}Вариант 3: Использовать SSH${NC}"
echo -e "1. Создайте SSH-ключ: ssh-keygen -t ed25519 -C \"your.email@example.com\""
echo -e "2. Добавьте ключ на GitHub: cat ~/.ssh/id_ed25519.pub"
echo -e "   Скопируйте вывод и добавьте на странице: https://github.com/settings/keys"
echo -e "3. Измените URL репозитория: git remote set-url origin git@github.com:Taulan23/labjava.git"
echo -e "4. Выполните: git push -u origin main"

echo -e "\n${GREEN}После загрузки ваш проект будет доступен по адресу:${NC}"
echo -e "${CYAN}https://github.com/Taulan23/labjava${NC}" 