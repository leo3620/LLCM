import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlocContainerComponent } from './bloc-container.component';

describe('BlocContainerComponent', () => {
  let component: BlocContainerComponent;
  let fixture: ComponentFixture<BlocContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BlocContainerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BlocContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
