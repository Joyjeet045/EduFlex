document.addEventListener("DOMContentLoaded", function() {
  const courseList = document.getElementById('course-list');
  const titleInput = document.getElementById('title');
  const categoryInput = document.getElementById('category');
  const instructorInput = document.getElementById('instructor');

  function loadCourses(title = '', category = '', instructorId = '') {
    const query = new URLSearchParams({ title, category, instructorId }).toString();
    fetch(`/api/courses/all?${query}`)
      .then(response => response.json())
      .then(courses => {
        displayCourses(courses);
      })
      .catch(error => console.error('Error fetching courses:', error));
  }

  function displayCourses(courses) {
    courseList.innerHTML = '';
    courses.forEach(course => {
      const courseDiv = document.createElement('div');
      courseDiv.className = 'course-card';
      courseDiv.innerHTML = `
        <h3>${course.title}</h3>
        <p>${course.description}</p>
        <p>Instructor: ${course.instructor.username}</p>
        <p>Price: $${course.price}</p>
        <p>Category: ${course.category}</p>
        <p>Language: ${course.language}</p>
      `;
      courseList.appendChild(courseDiv);
    });
  }

  titleInput.addEventListener('input', function() {
    loadCourses(this.value, categoryInput.value, instructorInput.value);
  });

  categoryInput.addEventListener('input', function() {
    loadCourses(titleInput.value, this.value, instructorInput.value);
  });

  instructorInput.addEventListener('input', function() {
    loadCourses(titleInput.value, categoryInput.value, this.value);
  });

  loadCourses();
});
