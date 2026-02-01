# ChromaDB 서버 실행 스크립트
#
# 사용법:
# 1. pip install chromadb 실행
# 2. 아래 명령어로 서버 실행:
#    chroma run --host localhost --port 8000 --path ./chroma_data
#
# 또는 이 스크립트 실행:
#    python start-chroma.py

import subprocess
import sys

if __name__ == "__main__":
    print("ChromaDB 서버를 시작합니다...")
    print("localhost:8000 포트에서 실행됩니다")
    print("종료하려면 Ctrl+C를 누르세요\n")

    try:
        subprocess.run([
            sys.executable, "-m", "chromadb.cli.cli",
            "run",
            "--host", "localhost",
            "--port", "8000",
            "--path", "./chroma_data"
        ])
    except KeyboardInterrupt:
        print("\n\nChromaDB 서버를 종료합니다.")
