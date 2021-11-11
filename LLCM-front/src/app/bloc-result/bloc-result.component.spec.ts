import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlocResultComponent } from './bloc-result.component';

describe('BlocResultComponent', () => {
  let component: BlocResultComponent;
  let fixture: ComponentFixture<BlocResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BlocResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BlocResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
