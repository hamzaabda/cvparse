import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BlogPost } from './blog-post.model'; // Assurez-vous de créer ce modèle

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private baseUrl = 'http://localhost:8080/auth/blogposts';

  constructor(private http: HttpClient) { }

  getAllBlogPosts(): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>(`${this.baseUrl}`);
  }

  getBlogPostById(id: number): Observable<BlogPost> {
    return this.http.get<BlogPost>(`${this.baseUrl}/${id}`);
  }

  createBlogPost(blogPost: BlogPost): Observable<BlogPost> {
    return this.http.post<BlogPost>(this.baseUrl, blogPost);
  }

  updateBlogPost(id: number, blogPost: BlogPost): Observable<BlogPost> {
    return this.http.put<BlogPost>(`${this.baseUrl}/${id}`, blogPost);
  }

  deleteBlogPost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
