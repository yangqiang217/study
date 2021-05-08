const s = new Set();
const s1 = new Set([1, 2, 3]);
//应用
//1.数组去重
const s3 = new Set([1, 1, 2, 2]);
s3.size;//2

//set -> array
const arr = [...s3];

//add()
//delete(value)
//has(value)
//clear()

//遍历
s1.forEach(value => {
    console.log(value);
});