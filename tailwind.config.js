/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/webapp/**/*.zul",   // Tất cả file ZUL
    "./src/main/webapp/**/*.html",  // Nếu có file HTML
    "./src/main/webapp/**/*.jsp",   // Nếu có JSP
    "./src/main/webapp/**/*.js",    // Nếu class nằm trong JS
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
