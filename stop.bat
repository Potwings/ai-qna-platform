@echo off
echo ====================================
echo QnA AI 컨테이너 종료
echo ====================================

echo.
echo ChromaDB 컨테이너 중지 중...
docker-compose down

echo.
echo 완료!
pause
