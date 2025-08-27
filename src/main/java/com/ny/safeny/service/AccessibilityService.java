package com.ny.safeny.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Accessibility Service
 * Provides accessibility metadata and WCAG 2.1 AA compliance features
 * Differentiator: NYS requires Section 508/WCAG compliance
 */
@Service
@Slf4j
public class AccessibilityService {
    
    /**
     * Get accessibility metadata for forms
     */
    public Map<String, String> getFormAccessibilityMetadata() {
        Map<String, String> metadata = new HashMap<>();
        
        // ARIA labels for screen readers
        metadata.put("disasterTypeLabel", "Select disaster type - Required field");
        metadata.put("descriptionLabel", "Describe damage and impact - Required field, minimum 20 characters");
        metadata.put("requestAmountLabel", "Enter request amount in dollars - Required field, minimum $100");
        metadata.put("submitButtonLabel", "Submit emergency relief application");
        
        // Error message templates for accessibility
        metadata.put("disasterTypeError", "Error: Disaster type is required. Please select one option.");
        metadata.put("descriptionError", "Error: Description must be between 20 and 2000 characters.");
        metadata.put("amountError", "Error: Amount must be between $100 and $50,000.");
        
        return metadata;
    }
    
    /**
     * Get color contrast ratios for WCAG 2.1 AA compliance
     */
    public Map<String, Object> getColorContrastRatios() {
        Map<String, Object> ratios = new HashMap<>();
        
        // NYS Standard Colors
        ratios.put("primaryBlue", "#0056b3");
        ratios.put("primaryBlueContrast", "7.5:1"); // Exceeds WCAG AA requirement of 4.5:1
        
        // High Contrast Mode
        ratios.put("highContrastBackground", "#000000");
        ratios.put("highContrastText", "#FFFFFF");
        ratios.put("highContrastYellow", "#FFFF00");
        ratios.put("highContrastRatio", "21:1"); // Maximum contrast
        
        return ratios;
    }
    
    /**
     * Get keyboard navigation shortcuts
     */
    public Map<String, String> getKeyboardShortcuts() {
        Map<String, String> shortcuts = new HashMap<>();
        
        shortcuts.put("skipToMain", "Alt + M - Skip to main content");
        shortcuts.put("focusSearch", "Alt + S - Focus on search field");
        shortcuts.put("toggleHighContrast", "Alt + H - Toggle high contrast mode");
        shortcuts.put("increaseFontSize", "Ctrl + Plus - Increase font size");
        shortcuts.put("decreaseFontSize", "Ctrl + Minus - Decrease font size");
        
        return shortcuts;
    }
    
    /**
     * Validate form for accessibility requirements
     */
    public AccessibilityValidationResult validateFormAccessibility(Map<String, Object> formData) {
        AccessibilityValidationResult result = new AccessibilityValidationResult();
        result.setCompliant(true);
        
        // Check if all required fields have ARIA labels
        if (!formData.containsKey("ariaLabel")) {
            result.setCompliant(false);
            result.addIssue("Missing ARIA labels for screen readers");
        }
        
        // Check if error messages are descriptive
        if (formData.containsKey("errors")) {
            Object errors = formData.get("errors");
            if (errors == null || errors.toString().isEmpty()) {
                result.addWarning("Error messages should be descriptive and actionable");
            }
        }
        
        log.info("Accessibility validation completed. Compliant: {}", result.isCompliant());
        return result;
    }
    
    /**
     * Get recommended font sizes for different screen readers
     */
    public Map<String, String> getRecommendedFontSizes() {
        Map<String, String> sizes = new HashMap<>();
        
        sizes.put("body", "16px"); // Minimum for readability
        sizes.put("heading1", "2rem");
        sizes.put("heading2", "1.75rem");
        sizes.put("buttonText", "1rem");
        sizes.put("errorMessage", "0.875rem");
        
        return sizes;
    }
    
    /**
     * Accessibility Validation Result DTO
     */
    public static class AccessibilityValidationResult {
        private boolean compliant;
        private final java.util.List<String> issues = new java.util.ArrayList<>();
        private final java.util.List<String> warnings = new java.util.ArrayList<>();
        
        public boolean isCompliant() {
            return compliant;
        }
        
        public void setCompliant(boolean compliant) {
            this.compliant = compliant;
        }
        
        public void addIssue(String issue) {
            this.issues.add(issue);
        }
        
        public void addWarning(String warning) {
            this.warnings.add(warning);
        }
        
        public java.util.List<String> getIssues() {
            return issues;
        }
        
        public java.util.List<String> getWarnings() {
            return warnings;
        }
    }
}
