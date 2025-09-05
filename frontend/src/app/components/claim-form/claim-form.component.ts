import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ClaimService } from '../../services/claim.service';

@Component({
  selector: 'app-claim-form',
  templateUrl: './claim-form.component.html',
  styleUrls: ['./claim-form.component.css']
})
export class ClaimFormComponent implements OnInit {
  claimForm!: FormGroup;
  loading = false;
  error = '';
  success = false;

  disasterTypes = [
    'Flood',
    'Fire',
    'Hurricane',
    'Tornado',
    'Earthquake',
    'Winter Storm',
    'Other'
  ];

  constructor(
    private fb: FormBuilder,
    private claimService: ClaimService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.claimForm = this.fb.group({
      disasterType: ['', Validators.required],
      incidentDate: ['', Validators.required],
      location: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(20)]],
      requestAmount: ['', [Validators.required, Validators.min(100)]]
    });
  }

  onSubmit(): void {
    if (this.claimForm.invalid) {
      this.error = 'Please fill in all required fields correctly';
      return;
    }

    this.loading = true;
    this.error = '';

    // Convert form data to proper format
    const formValue = this.claimForm.value;
    
    // Convert incidentDate to ISO string format
    const incidentDate = new Date(formValue.incidentDate);
    
    const claimData = {
      disasterType: formValue.disasterType,
      incidentDate: incidentDate.toISOString(),
      location: formValue.location,
      description: formValue.description,
      requestAmount: Number(formValue.requestAmount)
    };

    console.log('Submitting claim:', claimData);

    this.claimService.createClaim(claimData).subscribe({
      next: (response) => {
        console.log('Claim created:', response);
        this.success = true;
        this.loading = false;
        
        setTimeout(() => {
          this.router.navigate(['/my-claims']);
        }, 2000);
      },
      error: (error) => {
        console.error('Error creating claim:', error);
        this.error = error.error?.message || 'Failed to submit claim. Please try again.';
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard']);
  }

  getCurrentDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  get f() {
    return this.claimForm.controls;
  }
}
