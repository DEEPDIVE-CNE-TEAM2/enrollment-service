#!/bin/bash
# ==============================
# deploy-service.sh
# Gradle build + Docker build + ECR push 자동화
# 서비스별 변수 사용 가능
# ==============================

# --------- 사용자 정의 변수 ----------
SERVICE_NAME=${1:-enrollment-service}      # 첫 번째 인자: 서비스명, 기본값 auth-service
ECR_URI=${2:-004407157704.dkr.ecr.ap-northeast-2.amazonaws.com/$SERVICE_NAME} # 두 번째 인자: ECR URI
GRADLE_TASK=${3:-build}             # 세 번째 인자: gradle task, 기본값 build
AWS_REGION=${4:-ap-northeast-2}     # AWS 리전
TAG=$(date +%Y%m%d-%H%M%S)
# ------------------------------------

echo "=== 1️⃣ Gradle Build ==="
./gradlew $GRADLE_TASK || { echo "Gradle build failed"; exit 1; }

echo "=== 2️⃣ Docker Build ==="
docker build -t $SERVICE_NAME:$TAG . || { echo "Docker build failed"; exit 1; }

echo "=== 3️⃣ ECR 로그인 ==="
aws ecr get-login-password --region $AWS_REGION | \
docker login --username AWS --password-stdin $ECR_URI || { echo "ECR login failed"; exit 1; }

echo "=== 4️⃣ Docker Tag ==="
docker tag $SERVICE_NAME:$TAG $ECR_URI:$TAG || { echo "Docker tag failed"; exit 1; }
#docker tag $SERVICE_NAME:$TAG $ECR_URI:latest

echo "=== 5️⃣ Docker Push ==="
docker push $ECR_URI:$TAG || { echo "Docker push failed"; exit 1; }

echo "✅  Deployment steps completed for $SERVICE_NAME"
