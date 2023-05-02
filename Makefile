all:
	sudo docker build --tag gitoad .
	sudo docker run gitoad



export CRYPTO_ALGORITHM_CIPHER=AES/ECB/PKCS5Padding \
export CRYPTO_ALGORITHM_KEY=AES \
export CRYPTO_KEY='>Arx@fqNbSn58#P4' \
export DB_NAME=gitoad_db \
export DB_PASSWORD=gabka \
export DB_URL=jdbc:postgresql://localhost/gitoad_db \
export DB_USER=postgres \
export JWT_EXPIRATION=86400000 \
export JWT_SECRET=ThiS-Key_Is_very_too_so_secret-key \
export SECRET_KEY=11111111111111111111111111111111 \
export SPRING_SECURITY_PASSWORD=gumbaChumba2,, \
export SPRING_SECURITY_USERNAME=boBuk \