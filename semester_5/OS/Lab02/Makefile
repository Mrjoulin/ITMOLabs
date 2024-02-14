obj-m += kernel_module.o

reins:
	@echo "Reinstall module - START"
	make rm
	make build
	make ins
	@echo "Reinstall module - END"

build:
	@echo Build module in $(shell uname -r)
	make -C $(KERNEL_PATH)/lib/modules/$(shell uname -r)/build M=$(shell pwd) modules

clean:
	@echo "Clean module files"
	make -C $(KERNEL_PATH)/lib/modules/$(shell uname -r)/build M=$(PWD) clean

ins:
	@echo "Install module"
	sudo insmod kernel_module.ko

rm:
	@echo "Remove module"
	sudo rmmod kernel_module.ko

usr:
	gcc user_program.c -o user_program
