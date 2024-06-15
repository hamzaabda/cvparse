import { Component, OnInit } from '@angular/core';
import { BlogService } from './blog.service';
import { BlogPost } from './blog-post.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.scss']
})
export class BlogComponent implements OnInit {

  blogPosts: BlogPost[] = [];
  showCreateForm: boolean = false;
  newBlogPost: BlogPost = { title: '', content: '', createdAt: '', updatedAt: '' };

  constructor(private blogService: BlogService) { }

  ngOnInit(): void {
    this.loadBlogPosts();
  }

  loadBlogPosts(): void {
    this.blogService.getAllBlogPosts().subscribe(
      (data: BlogPost[]) => {
        this.blogPosts = data;
      },
      error => {
        console.error('Error fetching blog posts:', error);
      }
    );
  }

  createBlogPost(): void {
    this.showCreateForm = true;
    this.newBlogPost = { title: '', content: '', createdAt: '', updatedAt: '' };
  }

  submitCreatePostForm(): void {
    if (!this.newBlogPost.title.trim() || !this.newBlogPost.content.trim()) {
      Swal.fire('Error', 'Title and content are required', 'error');
      return;
    }

    this.blogService.createBlogPost(this.newBlogPost).subscribe(
      (createdPost: BlogPost) => {
        this.blogPosts.push(createdPost);
        Swal.fire('Success', 'Blog post created successfully', 'success');
        this.showCreateForm = false;
      },
      error => {
        console.error('Error creating blog post:', error);
        Swal.fire('Error', 'Failed to create blog post', 'error');
      }
    );
  }

  cancelCreatePost(): void {
    this.showCreateForm = false;
  }

  deleteBlogPost(blogPost: BlogPost): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this blog post!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.blogService.deleteBlogPost(blogPost.id).subscribe(
          () => {
            this.blogPosts = this.blogPosts.filter(post => post.id !== blogPost.id);
            Swal.fire('Deleted!', 'Your blog post has been deleted.', 'success');
          },
          error => {
            console.error('Error deleting blog post:', error);
            Swal.fire('Error', 'Failed to delete blog post', 'error');
          }
        );
      }
    });
  }
}
