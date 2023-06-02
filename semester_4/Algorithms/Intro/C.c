#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>

#define LINE_LEN 24

struct vars_list {
    char* name;
    char* value;
    struct vars_list* next;
};

struct block {
    struct vars_list* vars;
    struct block* prev;
};


void list_add_var(struct block* const block, char* const name, char* value) {
    struct vars_list* newList = malloc(sizeof(struct vars_list));

    if (newList) {
        newList->name = name;
        newList->value = value;
        newList->next  = block->vars;
        block->vars = newList;
    }
}

void list_cpy(struct block* const new_block, struct block* const prev_block) {
    if (!prev_block) return;

    struct vars_list* cur_var = prev_block->vars;
    while (cur_var) {
        list_add_var(new_block, cur_var->name, cur_var->value);
        cur_var = cur_var->next;
    }
}

struct block* block_create(struct block* const prev) {
    struct block* block = malloc(sizeof(struct block));

    if (block) {
        block->prev = prev;
        list_cpy(block, prev);
    }

    return block;
}

void update_var(struct block* const block, char* const var, char* const value) {
    struct vars_list* cur_var = block->vars;

    while (cur_var) {
        if (!strcmp(cur_var->name, var)) {
            cur_var->value = value;
            return;
        }
        cur_var = cur_var->next;
    }

    list_add_var(block, var, value);
}

void update_var_to_var(struct block* block, char* const var1, const char* const var2) {
    struct vars_list* var1_res = NULL;
    struct vars_list* var2_res = NULL;

    struct vars_list* cur_var = block->vars;

    while (cur_var && (!var1_res || !var2_res)) {
        if (!var1_res && !strcmp(cur_var->name, var1)) var1_res = cur_var;
        if (!var2_res && !strcmp(cur_var->name, var2)) var2_res = cur_var;

        cur_var = cur_var->next;
    }

    char* var1_value = var2_res ? var2_res->value : "0";

    printf("%s\n", var1_value);

    if (var1_res) var1_res->value = var1_value;
    else list_add_var(block, var1, var1_value);
}


int main() {
    char line[LINE_LEN];

    struct block* cur_block = block_create(NULL);

    while (scanf("%23[^\n]%*c", line) != EOF) {
        if (line[0] == '{') { // block process
            cur_block = block_create(cur_block);
        } else if (line[0] == '}') {
            cur_block = cur_block->prev;
        } else { // Var process
            char* var1 = malloc(12);
            char* var2 = malloc(12);
            char is_num = 1;

            int sep = 0;
            for (; line[sep] != '='; ++sep) var1[sep] = line[sep];
            var1[++sep] = 0;

            for (int i = sep; i < LINE_LEN; ++i) {
                var2[i - sep] = line[i];

                if (!line[i]) break;
                if (is_num && (line[i] < '0' || line[i] > '9') && line[i] != '-') is_num = 0;
            }

            if (is_num) { // var1 = number
                update_var(cur_block, var1, var2);
            } else { // var1 = var2
                update_var_to_var(cur_block, var1, var2);
            }
        }
    }
}