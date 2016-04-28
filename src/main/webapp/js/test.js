function X(a, b) {
  this.a = a;
  this.b = b;
}

var list = [];
list.push(new X(21, 1.90));
list.push(new X(1, 1.80));

var sum = function(list, f) {
  return list.reduce((acc, it) => acc + f(it), 0);
}

console.log("Sum a: " + sum(list, o => o.a));
console.log("Sum b: " + sum(list, o => o.b * 2));
