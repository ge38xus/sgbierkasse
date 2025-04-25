ECR_URI="383397309571.dkr.ecr.eu-north-1.amazonaws.com/prezzesik/bierkasse:latest"
IMAGE_NAME="prezzesik/bierkasse"

echo "🐳 Building Docker image..."
docker build --provenance false -t ${IMAGE_NAME} .
docker tag ${IMAGE_NAME}:latest ${ECR_URI}
echo "📦 Pushing image to ECR..."
docker push ${ECR_URI}
echo "✅ Image successfully pushed to ${ECR_URI}"