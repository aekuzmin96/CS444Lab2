import os

'''
4b 4d 32 39 76 48 63 68 62 45 43 73 79 46 59 42
ad be b3 00 13 6c 63 05 bf 21 eb 69 dc 71 e7 c0
0b 9c 6f 19 dd 78 94 79 e8 84 d2 9d a4 5c 0b ea
c4 9d 4a 57 01 c2 a9 03 e9 ce 0d 59 22 8e a4 d0
f0 03 a0 65 a9 ac 9e 04 f3 33 0e f9 03 be 54 dc
d7 30 7d bc 20 d9 6b c3 a3 e3 b4 5c 6a f1 44 7f
fe 86 cd eb a4 31 34 7e 6f 27 a8 80 15 a6 9d c0
95 65 75 4c dc 87 df 46 69 7f bd 98 18 4a 07 e5
b8 07 a0 8a c6 f3 56 16 50 c6 df 66 5c 4a 77 58
74 6f e7 b3 40 a2 2a 6d 01 d9 4f a8 b9 5d 98 3d
21 0e a4 e3 67 b5 f9 61 cc 93 40 7c 9b 05 98 a6
'''

partA = "python client.py -ip shasta.cs.unm.edu -p 10035 -b '"
partB = "210ea4e367b5f961cc93407c9b0598a6' -id 35"
IV = "746fe7b340a22a6d01d94fa8b95d98"

for i in range(256):
	if i < 16:
		hexValue = "0" + hex(i)[2:4]
	else:
		hexValue = hex(i)[2:4]
	os.system(partA + IV + hexValue + partB)
