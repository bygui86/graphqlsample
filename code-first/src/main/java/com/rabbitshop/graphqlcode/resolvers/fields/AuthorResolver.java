package com.rabbitshop.graphqlcode.resolvers.fields;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rabbitshop.graphqlcode.daos.Author;
import com.rabbitshop.graphqlcode.daos.Post;
import com.rabbitshop.graphqlcode.repos.PostRepo;
import com.rabbitshop.graphqlcode.resolvers.GraphQLInjectionFieldResolver;

import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * Sometimes, the value of a field is non-trivial to load. This might involve database lookups, complex calculations, or anything else. GraphQL Tools has a
 * concept of a field resolver that is used for this purpose. These are Spring beans that can provide values in place of the data bean.
 *
 * The field resolver is any bean in the Spring Context that has the same name as the data bean, with the suffix Resolver, and that implements the GraphQLResolver
 * interface. Methods on the field resolver bean follow all of the same rules as on the data bean but are also provided the data bean itself as a first parameter.
 *
 * If a field resolver and the data bean both have methods for the same GraphQL field then the field resolver will take precedence.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("authorResolver")
public class AuthorResolver implements GraphQLInjectionFieldResolver {
	
	@Resource(name = "postRepo")
	@Getter
	PostRepo postRepo;
	
	@GraphQLQuery(name = "posts")
	public List<Post> getPosts(
			@GraphQLContext final Author author) {
		
		log.debug("Get Posts for Author: " + author);

		return getPostRepo().getByAuthor(author.getId());
	}
	
}
