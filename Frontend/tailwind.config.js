/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}", // This is important for Angular
  ],
  theme: {
    extend: {},
  },
  plugins: [
    // require('@tailwindcss/postcss'),
  ],
}
