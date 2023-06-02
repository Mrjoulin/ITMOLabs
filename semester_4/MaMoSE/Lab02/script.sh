# Инициализируем
git init git_rep
cd git_rep

# r0 
sh ../first_user
I=0 && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r1
sh ../second_user
let "I += 1" && cp -r ../commits/commit$I/* ./
git checkout -b fork_r0
git add *
git commit -m "r$I"

# r2
sh ../first_user
git checkout -b fork_r1
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r3
sh ../second_user
git checkout fork_r0
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r4
sh ../first_user
git checkout -b fork_r3
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r5
git checkout main
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r6
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r7
git checkout fork_r1
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r8
sh ../second_user
git checkout fork_r0
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r9
git checkout -b fork_r8
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r10
sh ../first_user
git checkout fork_r1
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r11
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r12
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r13
git checkout fork_r3
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r14
git checkout fork_r1
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r15
sh ../second_user
git checkout fork_r0
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r16
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r17
sh ../first_user
git checkout fork_r1
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r18
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r19
git checkout fork_r3
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r20
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r21
git checkout main
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r22
git checkout fork_r3
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r23
sh ../second_user
git checkout fork_r8
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r24
sh ../first_user
git checkout fork_r1
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r25
sh ../second_user
git checkout fork_r0
git merge fork_r1 -m "merge fork_r1 with fork_r0"

# Resolve conflicts
nano *

let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r26
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r27
sh ../first_user
git checkout fork_r3
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r28
sh ../second_user
git checkout fork_r8
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r29
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r30
sh ../first_user
git checkout fork_r3
git merge fork_r8 -m "Merge fork_r8 with fork_r3"

# Resolve conflicts (copy right version)
echo "Copy right version, resolve conflict"

let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"
# r31
sh ../second_user
git checkout fork_r0
git merge fork_r3 -m "Merge fork_r3 with fork_r0"

# Resolve conflicts (copy right version)
echo "Copy right version, resolve conflict"

let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r32
sh ../first_user
git checkout main
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r33
sh ../second_user
git checkout fork_r0
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r34
let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"

# r35
sh ../first_user
git checkout main
git merge fork_r0 -m "Merge fork_r0 with main"

# Resolve conflicts (copy right version)
echo "Copy right version, resolve conflict"

let "I += 1" && cp -r ../commits/commit$I/* ./
git add *
git commit -m "r$I"
