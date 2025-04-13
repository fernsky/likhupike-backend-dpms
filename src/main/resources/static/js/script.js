// Common JavaScript functions

// Check if element exists before adding event listeners
function onElementReady(selector, callback) {
  const element = document.querySelector(selector);
  if (element) {
    callback(element);
  }
}

// Initialize all tooltips
document.addEventListener("DOMContentLoaded", function () {
  // Initialize Bootstrap tooltips
  const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
  );
  tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
  });
});
