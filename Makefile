all:
	docker build --tag gitoad .
	docker run gitoad