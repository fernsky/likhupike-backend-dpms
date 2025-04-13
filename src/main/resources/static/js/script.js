/**
 * Digital Profile System
 * Enhanced JavaScript for modern UI interactions
 */

// Main initialization function
document.addEventListener("DOMContentLoaded", function () {
  // Initialize all components
  initScrollAnimations();
  initSmoothScrolling();
  initLanguageSwitcher();
  initFormValidation();
  initEnhancedUI();
  initA11yImprovements();
});

/**
 * Initialize scroll-based animations with IntersectionObserver
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
          entry.target.style.animation = `fadeIn 0.6s ${
            delay * 0.15
          }s cubic-bezier(0.26, 0.86, 0.44, 0.985) forwards`;
          observer.unobserve(entry.target);
        }
      });
    },
    {
      threshold: 0.15,
      rootMargin: "0px 0px -50px 0px",
    }
  );

  fadeElems.forEach((elem) => {
    observer.observe(elem);
  });

  // Add parallax scroll effect
  const parallaxElements = document.querySelectorAll(
    ".hero-section, .cta-section"
  );

  window.addEventListener("scroll", function () {
    const scrolled = window.pageYOffset;

    parallaxElements.forEach((element) => {
      const elementInView =
        element.getBoundingClientRect().top < window.innerHeight &&
        element.getBoundingClientRect().bottom > 0;

      if (elementInView) {
        const elementTop = element.getBoundingClientRect().top;
        const speed = 0.5;
        const yPos = -(elementTop * speed) / 10;
        element.style.backgroundPosition = `center ${yPos}px`;
      }
    });
  });
}

/**
 * Initialize smooth scrolling for anchor links
 */
function initSmoothScrolling() {
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      const targetId = this.getAttribute("href");
      if (targetId === "#") return;

      const targetElement = document.querySelector(targetId);
      if (!targetElement) return;

      e.preventDefault();

      window.scrollTo({
        top: targetElement.offsetTop - 80,
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
    link.addEventListener("click", function () {
      // Get the language code from the href
      const langParam = this.href.split("lang=")[1];
      const lang = langParam ? langParam.split("&")[0] : "en";

      // Store the language preference in localStorage
      localStorage.setItem("preferredLanguage", lang);

      // Add a loading indicator
      document.body.classList.add("page-transitioning");

      // Continue with the normal link behavior (page will reload)
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
 * Initialize enhanced form validation
 */
function initFormValidation() {
  const contactForm = document.getElementById("contactForm");
  if (!contactForm) return;

  const formFields = contactForm.querySelectorAll("input, textarea");

  // Add real-time validation as user types
  formFields.forEach((field) => {
    field.addEventListener("input", function () {
      validateField(field);
    });

    field.addEventListener("blur", function () {
      validateField(field, true);
    });
  });

  contactForm.addEventListener("submit", function (e) {
    e.preventDefault();

    let isValid = true;

    // Validate all fields on submit
    formFields.forEach((field) => {
      if (!validateField(field, true)) {
        isValid = false;
      }
    });

    if (isValid) {
      // Show success message with animation
      const successMessage = document.createElement("div");
      successMessage.className = "alert alert-success mt-3 glass-card";
      successMessage.innerHTML =
        '<i class="fas fa-check-circle me-2"></i> Thank you for your message! Our team will get back to you soon.';
      successMessage.style.opacity = 0;
      successMessage.style.transform = "translateY(20px)";

      contactForm.appendChild(successMessage);

      // Trigger animation
      setTimeout(() => {
        successMessage.style.transition = "all 0.5s ease";
        successMessage.style.opacity = 1;
        successMessage.style.transform = "translateY(0)";
      }, 10);

      // Reset form with animation
      formFields.forEach((field) => {
        field.classList.add("is-valid");
        field.style.transition = "all 0.3s ease";
        field.style.transform = "translateX(0)";

        setTimeout(() => {
          field.style.transform = "translateX(10px)";
          setTimeout(() => {
            field.style.transform = "translateX(0)";
            field.value = "";
            setTimeout(() => {
              field.classList.remove("is-valid");
            }, 300);
          }, 300);
        }, 100);
      });

      // Remove success message after delay
      setTimeout(() => {
        successMessage.style.opacity = 0;
        successMessage.style.transform = "translateY(-20px)";

        setTimeout(() => {
          successMessage.remove();
        }, 500);
      }, 5000);
    }
  });

  function validateField(field, showError = false) {
    // Clear previous errors
    removeFieldError(field);

    // Get field value
    const value = field.value.trim();

    // Check if field is required
    if (field.hasAttribute("aria-required") && value === "") {
      if (showError) {
        addFieldError(
          field,
          `Please enter your ${field.previousElementSibling.textContent.toLowerCase()}`
        );
      }
      return false;
    }

    // Email validation
    if (field.type === "email" && value !== "") {
      const emailRegex =
        /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (!emailRegex.test(value)) {
        if (showError) {
          addFieldError(field, "Please enter a valid email address");
        }
        return false;
      }
    }

    // Field is valid
    if (value !== "") {
      field.classList.add("is-valid");
    }

    return true;
  }

  function addFieldError(field, message) {
    field.classList.add("is-invalid");

    const feedback = document.createElement("div");
    feedback.className = "invalid-feedback";
    feedback.textContent = message;

    field.parentNode.appendChild(feedback);

    // Subtle shake animation for visual feedback
    field.style.animation = "shake 0.5s cubic-bezier(.36,.07,.19,.97) both";
    field.addEventListener("animationend", () => {
      field.style.animation = "";
    });
  }

  function removeFieldError(field) {
    field.classList.remove("is-invalid");

    const feedback = field.parentNode.querySelector(".invalid-feedback");
    if (feedback) {
      feedback.remove();
    }
  }
}

/**
 * Initialize enhanced UI effects
 */
function initEnhancedUI() {
  // Animate stat counters
  const statNumbers = document.querySelectorAll(".stat-number");

  if (statNumbers.length) {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            animateCounter(entry.target);
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.5 }
    );

    statNumbers.forEach((stat) => {
      observer.observe(stat);
    });
  }

  function animateCounter(element) {
    const valueText = element.innerText;
    let finalValue = valueText;

    // Extract number portion for animation
    const numValue = parseFloat(valueText.replace(/[^0-9.]/g, ""));
    const suffix = valueText.replace(numValue.toString(), "");

    if (!isNaN(numValue)) {
      let startValue = 0;
      const duration = 2000;
      const startTime = performance.now();

      function updateCounter(currentTime) {
        const elapsedTime = currentTime - startTime;
        const progress = Math.min(elapsedTime / duration, 1);
        // Use easeOutExpo for smooth animation finish
        const easeProgress = 1 - Math.pow(1 - progress, 4);

        const currentValue = Math.floor(easeProgress * numValue);
        element.innerText = currentValue + suffix;

        if (progress < 1) {
          requestAnimationFrame(updateCounter);
        } else {
          element.innerText = finalValue;
        }
      }

      requestAnimationFrame(updateCounter);
    }
  }

  // Add floating effect to feature icons
  document.querySelectorAll(".feature-icon").forEach((icon) => {
    icon.style.animation = `float 3s ease-in-out infinite`;
    // Random delay for natural look
    icon.style.animationDelay = `${Math.random() * 2}s`;
  });

  // Define the float animation
  const style = document.createElement("style");
  style.textContent = `
    @keyframes float {
      0% { transform: translateY(0px); }
      50% { transform: translateY(-10px); }
      100% { transform: translateY(0px); }
    }
    
    @keyframes shake {
      10%, 90% { transform: translateX(-1px); }
      20%, 80% { transform: translateX(2px); }
      30%, 50%, 70% { transform: translateX(-4px); }
      40%, 60% { transform: translateX(4px); }
    }
  `;
  document.head.appendChild(style);

  // Added page transition effect
  window.addEventListener("beforeunload", function () {
    document.body.classList.add("page-transitioning");
  });
}

/**
 * Initialize accessibility improvements
 */
function initA11yImprovements() {
  // Ensure all interactive elements have appropriate ARIA attributes
  document
    .querySelectorAll('button:not([aria-label]):not([aria-hidden="true"])')
    .forEach((button) => {
      if (!button.textContent.trim()) {
        button.setAttribute("aria-label", "Button");
      }
    });

  // Add keyboard navigation for dropdown menus
  const dropdowns = document.querySelectorAll(".dropdown");
  dropdowns.forEach((dropdown) => {
    const trigger = dropdown.querySelector(".dropdown-toggle");
    const menu = dropdown.querySelector(".dropdown-menu");
    const items = menu ? menu.querySelectorAll(".dropdown-item") : [];

    if (trigger && menu && items.length) {
      trigger.addEventListener("keydown", function (e) {
        if (
          e.key === "ArrowDown" &&
          trigger.getAttribute("aria-expanded") === "true"
        ) {
          e.preventDefault();
          items[0].focus();
        }
      });

      items.forEach((item, index) => {
        item.addEventListener("keydown", function (e) {
          if (e.key === "ArrowDown" && index < items.length - 1) {
            e.preventDefault();
            items[index + 1].focus();
          } else if (e.key === "ArrowUp") {
            e.preventDefault();
            if (index > 0) {
              items[index - 1].focus();
            } else {
              trigger.focus();
            }
          } else if (e.key === "Escape") {
            e.preventDefault();
            trigger.focus();
            // Close dropdown using Bootstrap API
            bootstrap.Dropdown.getInstance(trigger).hide();
          }
        });
      });
    }
  });
}
