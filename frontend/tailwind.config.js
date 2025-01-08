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
        primary: '#813695',
        secondary: '#6b2e7a',
        accent: '#ce90e2',
        danger: '#381141',
        background: '#ffff',
        text: '#0b060d',
      },
      spacing: {
        formPadding: '32px', // Reusable spacing variable
      },
    },
  },
  plugins: [],
};
