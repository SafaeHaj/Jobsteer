/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './public/index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        ui: ['Roboto', 'Arial', 'sans-serif'], 
      },
      colors: {
        primary: '#5D2A68',
        accent: '#B786C8',
        danger: '#cd1c18',
        text: '#5D2A68',
      },
      spacing: {
        formPadding: '32px', // Reusable spacing variable
      },
    },
  },
  plugins: [],
};
