@echo off
echo "ע�⣺sql�ļ�·���в��ܴ�������"
set /p sql_path=������ִ��sql�����ļ��У�
SET /p dbhost=��������
SET /p dbuser=�û�����
SET /p dbpasswd=�û����룺
SET /p dbname=���ݿ����ƣ�
SET /p dbport=���ݿ�˿ڣ�

if not exist "%sql_path%\" echo ( �ļ���·����Ч & pause&exit )

mysql -h%dbhost% -u%dbuser% -p%dbpasswd% -P%dbport% -e "create database %dbname%"

for /r "%sql_path%" %%j in (*.sql) do (
    echo "%%j��ʼִ��"
    mysql -h%dbhost% -u%dbuser% -p%dbpasswd% -P%dbport% -D%dbname% --default-character-set=utf8 -e "source %%j"
    echo "���"
)
pause >nul