/**
 * Digital Profile System
 * Main JavaScript file for enhancing user experience
 */

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

  // Initialize scroll-based animations
  initScrollAnimations();

  // Initialize smooth scrolling for anchor links
  initSmoothScrolling();

  // Initialize language switcher
  initLanguageSwitcher();

  // Initialize form validation
  initFormValidation();
});

/**
 * Initialize scroll-based fade-in animations
 */
function initScrollAnimations() {
  const fadeElems = document.querySelectorAll(".fade-in");

  if (fadeElems.length === 0) return;

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const delay =
            entry.target.style.getPropertyValue("--animation-order") || 0;
          entry.target.style.transitionDelay = `${delay * 0.1}s`;
          entry.target.style.opacity = "1";
          entry.target.style.transform = "translateY(0)";
          observer.unobserve(entry.target);
        }
      });
    },
    {
      threshold: 0.1,
      rootMargin: "0px 0px -50px 0px",
    }
  );

  fadeElems.forEach((elem) => {
    observer.observe(elem);
  });
}

/**
 * Initialize smooth scrolling for anchor links
 */
function initSmoothScrolling() {
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault();

      const targetId = this.getAttribute("href");
      if (targetId === "#") return;

      const targetElement = document.querySelector(targetId);
      if (!targetElement) return;

      window.scrollTo({
        top: targetElement.offsetTop - 80, // Adjust for fixed header
        behavior: "smooth",
      });

      // Update URL but don't scroll again
      history.pushState(null, null, targetId);
    });
  });
}

/**
 * Initialize language switcher behavior
 */
function initLanguageSwitcher() {
  const languageLinks = document.querySelectorAll(
    '.dropdown-menu a[href*="lang="]'
  );

  languageLinks.forEach((link) => {
    link.addEventListener("click", function (e) {
      // Get the language code from the href
      const langParam = this.href.split("lang=")[1];
      const lang = langParam ? langParam.split("&")[0] : "en";

      // Store the language preference in localStorage
      localStorage.setItem("preferredLanguage", lang);

      // Continue with the normal link behavior
      // The page will reload with the new language parameter
    });
  });

  // Apply saved language preference on page load if not already set
  const currentLang = new URLSearchParams(window.location.search).get("lang");
  if (!currentLang) {
    const savedLang = localStorage.getItem("preferredLanguage");
    if (savedLang) {
      // Redirect to the same URL but with the language parameter
      const url = new URL(window.location.href);
      url.searchParams.set("lang", savedLang);
      window.location.href = url.toString();
    }
  }
}

/**
 * Initialize form validation for contact form
 */
function initFormValidation() {
  const contactForm = document.getElementById("contactForm");
  if (!contactForm) return;

  contactForm.addEventListener("submit", function (e) {
    e.preventDefault();

    // Get form fields
    const name = document.getElementById("name");
    const email = document.getElementById("email");
    const subject = document.getElementById("subject");
    const message = document.getElementById("message");
    let isValid = true;

    // Simple validation
    if (!name.value.trim()) {
      markInvalid(name, "Please enter your name");
      isValid = false;
    } else {
      markValid(name);
    }

    if (!email.value.trim()) {
      markInvalid(email, "Please enter your email");
      isValid = false;
    } else if (!isValidEmail(email.value.trim())) {
      markInvalid(email, "Please enter a valid email address");
      isValid = false;
    } else {
      markValid(email);
    }

    if (!subject.value.trim()) {
      markInvalid(subject, "Please enter a subject");
      isValid = false;
    } else {
      markValid(subject);
    }

    if (!message.value.trim()) {
      markInvalid(message, "Please enter your message");
      isValid = false;
    } else {
      markValid(message);
    }

    // If the form is valid, show success message
    if (isValid) {
      // Here you would normally send the form data to the server
      showFormSuccess(contactForm);
    }
  });
}

/**
 * Validate email format
 */
function isValidEmail(email) {
  const re =
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
}

/**
 * Mark form field as invalid
 */
function markInvalid(field, message) {
  field.classList.add("is-invalid");

  // Create or update feedback div
  let feedback = field.nextElementSibling;
  if (!feedback || !feedback.classList.contains("invalid-feedback")) {
    feedback = document.createElement("div");
    feedback.className = "invalid-feedback";
    field.parentNode.insertBefore(feedback, field.nextSibling);
  }
  feedback.textContent = message;
}

/**
 * Mark form field as valid
 */
function markValid(field) {
  field.classList.remove("is-invalid");
  field.classList.add("is-valid");

  // Remove any existing feedback
  const feedback = field.nextElementSibling;
  if (feedback && feedback.classList.contains("invalid-feedback")) {
    feedback.textContent = "";
  }
}

/**
 * Show form success message
 */
function showFormSuccess(form) {
  // Create success alert
  const successAlert = document.createElement("div");
  successAlert.className = "alert alert-success mt-3";
  successAlert.innerHTML =
    '<i class="fas fa-check-circle me-2"></i> Thank you for your message! We will get back to you soon.';

  // Insert before form
  form.parentNode.insertBefore(successAlert, form.nextSibling);

  // Reset the form
  form.reset();

  // Remove valid class from all inputs
  form.querySelectorAll(".is-valid").forEach((input) => {
    input.classList.remove("is-valid");
  });

  // Remove the success message after a delay
  setTimeout(() => {
    successAlert.classList.add("fade");
    setTimeout(() => successAlert.remove(), 500);
  }, 5000);
}
