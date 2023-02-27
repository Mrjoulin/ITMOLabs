CFLAGS     = -g -O2 -Wall -Werror -std=c17
CC         = gcc
LD         = gcc

IDIR       = ./include
CFLAGS     = -I$(IDIR)
_DEPS      = input.h solve.h utils.h
DEPS       = $(patsubst %,$(IDIR)/%,$(_DEPS))

ODIR       = out
OCOMPDIR   = $(ODIR)/lib
_OUTS      = input.o main.o solve.o utils.o
OUTS       = $(patsubst %,$(OCOMPDIR)/%,$(_OUTS))

TARGET     = system_solver

run: $(TARGET)
	./$(ODIR)/$(TARGET)

all: $(TARGET)

$(TARGET): $(OUTS)
	mkdir -p $(ODIR)
	$(LD) -o $(ODIR)/$@ $^

$(OCOMPDIR)/%.o: src/%.c $(DEPS)
	mkdir -p $(OCOMPDIR)
	$(CC) -c $(CFLAGS) -o $@ $<

clean: 
	rm -rf $(ODIR)/$(TARGET) $(OCOMPDIR)/*.o

.PHONY: clean all run
