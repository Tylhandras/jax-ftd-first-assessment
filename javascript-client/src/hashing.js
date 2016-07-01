import bcrypt from 'bcryptjs'

export function hash (password) {
  return new Promise(function executor (resolve, reject) { // function does not need to be named
    bcrypt.genSalt(function (err, salt) {
      if (err) {
        reject(err)
      } else {
        bcrypt.hash(password, salt, function (err, hashedPassword) {
          if (err) {
            reject(err)
          } else {
            resolve(hashedPassword)
          }
        })
      }
    })
  })
}

export function compare (plaintextPassword, hashedPassword) {
  return new Promise(function executor (resolve, reject) { // does not need to be named
    bcrypt.compare(plaintextPassword, hashedPassword, function (err, successFlag) {
      if (err) {
        reject(err)
      } else {
        resolve(successFlag)
      }
    })
  })
}

export default {
  hash,
  compare
}
