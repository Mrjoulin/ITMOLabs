#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/fs.h>
#include <linux/memblock.h>
#include <linux/mm.h>
#include <linux/netdevice.h>
#include <linux/uaccess.h>
#include <linux/printk.h>
#include <net/net_namespace.h>
#include <net/sock.h>
#include <net/tcp.h>
#include <net/udp.h>
#include <net/udplite.h>
#include <net/raw.h>

#define BUFFER_SIZE 1024

static char proc_buffer[BUFFER_SIZE];
static struct proc_dir_entry *proc_entry;


static void format_bytes(unsigned long long bytes, char *buf, size_t buf_size) {
    const char *suffixes[] = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
    const int devider = 1024;
    int suffixIndex = 0;
    bytes = bytes * 10;

    while (bytes >= devider * 10 && suffixIndex < sizeof(suffixes)/sizeof(suffixes[0])) {
        bytes = (bytes + devider / 2) / devider;
        suffixIndex++;
    }

    if (bytes % 10 == 0) {
        snprintf(buf, buf_size,"%llu%s", bytes / 10, suffixes[suffixIndex]);
    } else {
        snprintf(buf, buf_size, "%llu.%llu%s", bytes / 10, bytes % 10, suffixes[suffixIndex]);
    }
}


static int write_memblok_type_info(void) {
    struct memblock_type mmb_type = memblock.memory;

    char readable_bytes[10];
    format_bytes(mmb_type.total_size, readable_bytes, 10);

    return snprintf(
        proc_buffer,
        BUFFER_SIZE,
        "Memblock Type:\n"
        "Symbolic name: %s\n"
        "Number of allocated regions: %lu\n"
        "Max number of regions: %lu\n"
        "Size of all regions: %s (%llu B)\n",
        mmb_type.name, mmb_type.cnt, mmb_type.max,
        readable_bytes, mmb_type.total_size
    );
}

static int write_socket_info(void) {
    struct net *net = get_net_ns_by_pid(1);

    return snprintf(
        proc_buffer,
        BUFFER_SIZE,
        "Socket:\n"
        "TCP: inuse: %d\n"
        "UDP: inuse: %d\n"
        "UDPLITE: inuse: %d\n"
        "RAW: inuse: %d\n",
        sock_prot_inuse_get(net, &tcp_prot),
        sock_prot_inuse_get(net, &udp_prot),
        sock_prot_inuse_get(net, &udplite_prot),
        sock_prot_inuse_get(net, &raw_prot)
    );
}

static ssize_t read_proc(struct file *filp, char *buffer, size_t length, loff_t *offset) {
    int ret = 0;

    if (*offset > 0) {
        return 0;
    }

    if (strcmp(proc_buffer, "memblock_type") == 0) {
        write_memblok_type_info();
    } else if (strcmp(proc_buffer, "socket") == 0) {
        write_socket_info();
    } else {
        sprintf(proc_buffer, "Invalid argument\n");
    }

    ret = simple_read_from_buffer(buffer, length, offset, proc_buffer, strlen(proc_buffer));

    return ret;
}

static ssize_t write_proc(struct file *filp, const char *buffer, size_t length, loff_t *offset) {
    if (length > BUFFER_SIZE) {
        length = BUFFER_SIZE;
    }

    if (copy_from_user(proc_buffer, buffer, length)) {
        return -EFAULT;
    }

    proc_buffer[length] = '\0';

    return length;
}

static const struct proc_ops proc_fops = {
    .proc_read = read_proc,
    .proc_write = write_proc,
};

static int __init my_kernel_module_init(void) {
    proc_entry = proc_create("my_kernel_module", 0666, NULL, &proc_fops);
    if (proc_entry == NULL) {
        printk(KERN_ERR "Error creating /proc/my_kernel_module entry\n");
        return -ENOMEM;
    }

    printk(KERN_INFO "/proc/my_kernel_module created\n");

    return 0;
}

static void __exit my_kernel_module_exit(void) {
    proc_remove(proc_entry);
    printk(KERN_INFO "/proc/my_kernel_module removed\n");
}

module_init(my_kernel_module_init);
module_exit(my_kernel_module_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("joulin");
MODULE_DESCRIPTION("Kernel module for retrieving information about memblock_type and socket structures");
