all:
	sudo docker build --tag gitoad .
	sudo docker run gitoad