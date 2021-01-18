# Hunterlang
A hunter x hunter themed interpreted esoteric programming language.  

Very basic functionality is available:
```
killua0 hunts 4             # save 4 to memory location 0
Leorio does say killua9     # output the saved data at location 0
Wing does dump              # dump memory
Gon wants true {            # if true
    Leorio does say "hi"    # output hi
}
```
Some example code (that does not work yet):
```	
#Add two numbers
Leorio does say "Number 1"
killua0 hunts Leorio does listen
Leorio does say "Number 2"
killua2 hunts Leorio does listen
killua3 hunts killua0 + killua1
Leorio does say killua3
```
	
  
```
#Output the smaller number or both if they are equal
Leorio does say "Number 1"
killua0 hunts Leorio does listen
Leorio does say "Number 2"
killua2 hunts Leorio does listen
Gon wants killua0 > killua1 {
    Leorio does say killua1
} wants killua1 > killua0 {
   Leorio does say killua0
} got {
   Leorio does say killua0 + " " + killua1
}
```
	

```
#Read in 2 numbers using a function and output their sum with a function
Read2 Zoldyck gets 0 does {
  Leorio does say "Number 1"
  killua0 hunts Leorio does listen
  Leorio does say "Number 2"
  killua2 hunts Leorio does listen
}

Add Zoldyck gets 2 gives {
   illumi0 + illumi1
}

Read2
Leorio does say add killua0 killua1
```

sadly, Github syntax highlighting is not available yet
