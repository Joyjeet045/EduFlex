import { languages, categories } from './utils.js';

function loadOptions() {
  const languageSelect = document.getElementById('language');
  languages.forEach(lang => {
    const option = document.createElement('option');
    option.value = lang;
    option.textContent = lang;
    languageSelect.appendChild(option);
  });

  const categorySelect = document.getElementById('category');
  categories.forEach(cat => {
    const option = document.createElement('option');
    option.value = cat;
    option.textContent = cat;
    categorySelect.appendChild(option);
  });
}
document.addEventListener("DOMContentLoaded", function() {
  loadOptions();
});