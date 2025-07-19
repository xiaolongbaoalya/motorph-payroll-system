## 🧩 Terminal Assessment Overview

Following the mentor feedback meeting, **dev-felice** created a Canva slide to document and clarify all the miscommunications and overlooked items from the change requests. This helped the team align more efficiently moving forward.

**dev-shane** shared a Figma link proposing an alternative UI design, but due to time and implementation constraints, the team decided to proceed with polishing the original design.

The team then addressed the minor improvements suggested during the feedback session, including theme spacing and layout adjustments to refine the user interface.

- **dev-shane**: Acknowledged the issue raised in the change request and fixed the layout of the JTable to properly display the employee information pop-up when a user selects an employee.

- **dev-aly**: Implemented a dropdown menu for payroll calculation and updated the navigation menu to the finalized version based on the desired UI design.

- **dev-christine**: Fixed the dropdown display issue in the Payroll Calculation section and corrected its UI spacing inconsistencies.

- **dev-felice**: Began QA testing and caught a bug where supervisor names were not appearing in the records. This issue was resolved by **dev-christine**, who updated the relevant display logic.

- **dev-aly**: Started UI polishing and identified a critical bug that allowed both existing and newly added employees to access data not present in the records. This was escalated to **dev-red** for backend validation fixes.

- **dev-red**: Resolved the employee data access issue by updating backend handling to prevent invalid record retrieval and improve data integrity.

- **dev-aly**: Added a global UI helper to centralize theming across the application and passed it to **dev-felice** and **dev-shane** for further usability testing.

- **dev-felice**: Fixed minor UI inconsistencies found during QA and verified the application’s visual and interactive consistency with the updated theme.

- **dev-shane**: Improved unclear error pop-ups to provide more informative user feedback during form validation and invalid operations.

- **dev-christine**: Performed final code cleanup by removing unused methods and imports, and successfully integrated **dev-shane**’s GUI module into the main application.

- **dev-red**: Gave the project a final review and go signal for submission.
