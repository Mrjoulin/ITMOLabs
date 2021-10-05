program HammingCode
  implicit none
  integer, parameter :: codeLength = 7
  integer, parameter :: checkBits = 3
  integer, parameter :: informationBitsLen = codeLength - checkBits
  integer :: wrongBit
  integer :: cur
  integer :: i
  integer :: j

  integer :: codeArray(codeLength)
  integer :: syndromeS(checkBits)
  integer :: informationBits(informationBitsLen)
  character(codeLength) :: str

  syndromeS(:) = 0

  write (*, "(A)", advance='no') "Input the code: "
  read(*, *) str

  ! Fill code array

  do i = 1, codeLength
      if (str(i:i) == "0") codeArray(i) = 0
      if (str(i:i) == "1") codeArray(i) = 1
  end do

  ! Count syndrome s for each check bit

  do i = 1, codeLength
      do j = 1, checkBits
          if (mod(i, 2 ** j) >= 2 ** (j - 1)) then
              syndromeS(j) = syndromeS(j) + codeArray(i)
          end if
      end do
  end do

  ! Find the wrong bit

  wrongBit = 0

  do i = 0, checkBits - 1
      wrongBit = wrongBit + mod(syndromeS(i + 1), 2) * 2 ** i
  end do

  ! Invert wrong bit in the message

  if (wrongBit > 0) codeArray(wrongBit) = 1 - codeArray(wrongBit)

  ! Find all information bits

  cur = 1

  first_loop: do i = 1, codeLength
      do j = 0, checkBits - 1
         if (i == 2 ** j) cycle first_loop
      end do

      informationBits(cur) = codeArray(i)
      cur = cur + 1
  end do first_loop

  ! Print right message, information bits and wrong bit number

  write(*, *) "" ! New line
  write(*, "(A)", advance='no') "Right Message: "
  do i = 1, codeLength
      write(*, "(I0)", advance='no') codeArray(i)
  end do

  write(*, *) "" ! New line
  write(*, "(A)", advance='no') "Information Bits: "

  do i = 1, informationBitsLen
        write(*, "(I0)", advance='no') informationBits(i)
  end do

  write(*, *) "" ! New line

  if (wrongBit > 0) then
     write(*, "(A, I1)") "Wrong Bit: ", wrongBit
  else
     write(*, "(A)") "No Wrong Bis"
  end if
end program HammingCode