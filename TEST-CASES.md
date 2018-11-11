# crypto-trivium - test cases

## Tests Cases
- Encrypt/Decrypt files 
    - Using more than 10 characters in Key and IV (Key: LALAPEPELALA - IV: 012345678910)
        - [X] Encrypt text file using output file returns an encrypted file and after process that file again returns to the original file.
        - [x] Encrypt png image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt bmp image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt jpg image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt text file using and then trying to decrypt using different chars in the last characters in the key (the chars 11-n) should work fine.
           
    - Using less than 10 characters in Key and IV (Key: LALAPEPELA - IV: 0123456789)
        - [X] Encrypt text file using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt png image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt bmp image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt jpg image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt text file using an IV which is empty works fine and you can  decrypt that file as expected. 
    
    - Using 10 characters in Key and IV (Key: LALA - IV: 012)
        - [X] Encrypt text file using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt png image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt bmp image using output file returns an encrypted file and after process that file again returns to the original file.
        - [X] Encrypt jpg image using output file returns an encrypted file and after process that file again returns to the original file.
    
    - Using different key encrypting than decrypting (Encrypt Key: LALAPEPELALA - IV: 012345678910 -> Decrypt Key: EALAPEPELALA - IV: 012345678910)
        - [X] Encrypt text file using output file returns an encrypted file and after process that file again returns a different file.
        - [X] Encrypt png image using output file returns an encrypted file and after process that file again returns a different file.
        - [X] Encrypt bmp image using output file returns an encrypted file and after process that file again returns a different file.
        - [X] Encrypt jpg image using output file returns an encrypted file and after process that file again returns a different file.

    - Using Output bits with a value smaller than the original file.
        - [X] If the amount of output bits is bigger than the length of the original file the output bits value is ignored. 

    - Using Output bits with a value smaller than the original file.
        - [X] the file is going to encrypted just to the amount of bits selected. After decrypting it the file will be visible correctly but just to the amount of bits in the output bits.

    - Using Output bits with a value smaller than the original file.
        - [X] the file is going to encrypted just to the amount of bits selected. After decrypting it the file will be visible correctly but just to the amount of bits in the output bits.
        
    - Using BMP file encrypt, then modify the encrypted file, finally decrypt the modified file and a different image should appear
        - [x] It works as expected but the decrypted image is just noise. It does not seem similar to the original image. 
 
    
- Generate random Output bits (Input file empty, Output bits populated)
    - [X] Generate bits with Key and IV works fine
    - [X] Generate bits several times with the same Key and IV returns the same result
    - [X] Generate bits with Key and empty IV works fine
    - [X] Generate bits with Key and IV generates a file with the amount of output bits, then encrypting that files generates an empty file, finally decrypting that empty file generates the original file.