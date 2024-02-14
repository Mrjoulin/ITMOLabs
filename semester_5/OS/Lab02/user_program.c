#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_SIZE 1024

int main(int argc, char **argv) {
    if (argc != 2) {
        printf("Usage: %s [memblock_type/socket]\n", argv[0]);
        exit(1);
    }

    char buffer[BUFFER_SIZE];
    memset(buffer, 0, BUFFER_SIZE);

    FILE *fp = fopen("/proc/my_kernel_module", "w");
    if (fp == NULL) {
        printf("Error opening /proc/my_kernel_module\n");
        exit(1);
    }

    fprintf(fp, "%s", argv[1]);
    fclose(fp);

    fp = fopen("/proc/my_kernel_module", "r");
    if (fp == NULL) {
        printf("Error opening /proc/my_kernel_module\n");
        exit(1);
    }

    while (fgets(buffer, BUFFER_SIZE, fp)) {
        printf("%s", buffer);
    }

    fclose(fp);

    return 0;
}

