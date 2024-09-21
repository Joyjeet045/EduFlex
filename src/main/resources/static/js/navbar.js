document.addEventListener("DOMContentLoaded", function() {
  const greetingElement = document.getElementById('greeting');
  const welcomeElement = document.getElementById('welcomeMessage');
  const createCourseLink = document.getElementById('createCourse');

  function getGreeting() {
    const hours = new Date().getHours();
    if (hours < 12) {
      return "Good Morning";
    } else if (hours < 18) {
      return "Good Afternoon";
    } else {
      return "Good Evening";
    }
  }
  fetch('/api/users/current').then(response => {
    if (!response.ok) throw new Error('Error fetching current user');
    return response.json();
  })
  .then(data=>{
    greetingElement.innerText = getGreeting();
    welcomeElement.innerText = `, ${data.username}!`;
    if (data.role === "TEACHER") {
      createCourseLink.style.display = 'inline';
    } else {
      createCourseLink.style.display = 'none';
    }
  })
  .catch(error => {
    console.error('Error:', error);
    greetingElement.innerText = getGreeting();
    welcomeElement.innerText = ", Guest!";
    createCourseLink.style.display = 'none';
  });
})