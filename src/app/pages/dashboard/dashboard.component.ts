import { Component, OnInit } from '@angular/core';
import { CalendarService } from './calendar.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  public days: string[] = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  public currentMonth: string;
  public currentYear: number;
  public dates: number[] = [];
  public monthNames: string[] = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];
  
  private monthIndex: number;
  public selectedDate: number | null = null;
  public eventTitle: string = '';
  public eventDescription: string = '';
  public events: any[] = [];

  constructor(private calendarService: CalendarService) {
    const today = new Date();
    this.currentYear = today.getFullYear();
    this.monthIndex = today.getMonth();
    this.currentMonth = this.monthNames[this.monthIndex] + ' ' + this.currentYear;
  }

  ngOnInit(): void {
    this.generateCalendar();
    this.loadEvents();
  }

  generateCalendar(): void {
    const firstDayOfMonth = new Date(this.currentYear, this.monthIndex, 1).getDay();
    const daysInMonth = new Date(this.currentYear, this.monthIndex + 1, 0).getDate();

    this.dates = [];
    
    for (let i = 0; i < firstDayOfMonth; i++) {
      this.dates.push(null);
    }
    
    for (let day = 1; day <= daysInMonth; day++) {
      this.dates.push(day);
    }
  }

  changeMonth(direction: number): void {
    this.monthIndex += direction;
    if (this.monthIndex > 11) {
      this.monthIndex = 0;
      this.currentYear++;
    } else if (this.monthIndex < 0) {
      this.monthIndex = 11;
      this.currentYear--;
    }
    this.currentMonth = this.monthNames[this.monthIndex] + ' ' + this.currentYear;
    this.generateCalendar();
    this.loadEvents();
  }

  selectDate(date: number): void {
    this.selectedDate = date;
  }

  createEvent(): void {
    if (this.selectedDate !== null && this.eventTitle && this.eventDescription) {
      const event = {
        title: this.eventTitle,
        description: this.eventDescription,
        date: `${this.currentYear}-${this.monthIndex + 1}-${this.selectedDate}`
      };
      this.calendarService.createEvent(event).subscribe(() => {
        this.eventTitle = '';
        this.eventDescription = '';
        this.selectedDate = null;
        this.loadEvents(); // Reload events after creating a new one
      });
    }
  }

  loadEvents(): void {
    this.calendarService.getEvents().subscribe(data => {
      // Filter events to show only for the current month
      this.events = data.filter(event => {
        const eventDate = new Date(event.date);
        return eventDate.getFullYear() === this.currentYear && eventDate.getMonth() === this.monthIndex;
      });
    });
  }
}
