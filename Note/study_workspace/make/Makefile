objects = main.o kbd.o command.o display.o insert.o search.o files.o utils.o

edit : $(objects)
	cc -o edit $(objects) #把各种.o打包到一起，命名为edit

	@echo compling main...
main.o 		: defs.h
kbd.o 		: defs.h command.h
command.o 	: defs.h command.h
display.o 	: defs.h buffer.h
insert.o 	: defs.h buffer.h
search.o 	: defs.h buffer.h
files.o 	: defs.h buffer.h command.h
utils.o 	: defs.h

.PHONY 		: clean
clean :
	-rm edit $(objects) #减号的意思就是，也许某些文件出现问题，但不要管，继续做后面的事