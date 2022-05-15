################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/CIPMultiRequestProtocol.c \
../src/CNPath.c \
../src/Encapsulation.c \
../src/MessageRouterProtocol.c \
../src/protocol.c 

C_DEPS += \
./src/CIPMultiRequestProtocol.d \
./src/CNPath.d \
./src/Encapsulation.d \
./src/MessageRouterProtocol.d \
./src/protocol.d 

OBJS += \
./src/CIPMultiRequestProtocol.o \
./src/CNPath.o \
./src/Encapsulation.o \
./src/MessageRouterProtocol.o \
./src/protocol.o 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.c src/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	arm-linux-gnueabihf-gcc -O0 -g3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src

clean-src:
	-$(RM) ./src/CIPMultiRequestProtocol.d ./src/CIPMultiRequestProtocol.o ./src/CNPath.d ./src/CNPath.o ./src/Encapsulation.d ./src/Encapsulation.o ./src/MessageRouterProtocol.d ./src/MessageRouterProtocol.o ./src/protocol.d ./src/protocol.o

.PHONY: clean-src

