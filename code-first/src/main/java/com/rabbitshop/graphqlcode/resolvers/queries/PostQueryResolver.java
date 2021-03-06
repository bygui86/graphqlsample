package com.rabbitshop.graphqlcode.resolvers.queries;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rabbitshop.graphqlcode.annotations.GraphQLInjectionQueryResolverAnnotation;
import com.rabbitshop.graphqlcode.daos.Post;
import com.rabbitshop.graphqlcode.repos.PostRepo;
import com.rabbitshop.graphqlcode.resolvers.GraphQLInjectionQueryResolver;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * The root query needs to have special beans defined in the Spring context to handle the various fields in this root query. Unlike the schema definition, there is no
 * restriction that there only be a single Spring bean for the root query fields.
 *
 * The only requirements are that the beans implement GraphQLQueryResolver and that every field in the root query from the scheme has a method in one of these classes
 * with the same name.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("postQueryResolver")
@GraphQLInjectionQueryResolverAnnotation
public class PostQueryResolver implements GraphQLInjectionQueryResolver {
	
	@Resource(name = "postRepo")
	@Getter
	PostRepo postRepo;

	@GraphQLQuery(name = "allPosts")
	public List<Post> getAllPosts() {

		log.debug("Get all Posts");
		
		return getPostRepo().getAll();
	}
	
	/**
	 * The names of the method must be one of the following, in this order:
	 *	. <field>
	 *	. is<field> – only if the field is of type Boolean
	 *	. get<field>
	 *
	 * The method must have parameters that correspond to any parameters in the GraphQL schema, and may optionally take a final parameter of type DataFetchingEnvironment.
	 *
	 * The method must also return the correct return type for the type in the GraphQL scheme, as we are about to see. Any simple types – String, Int, List, etc. – can be
	 * used with the equivalent Java types, and the system just maps them automatically.
	 *
	 * @param count
	 * @param offset
	 *
	 * @return List of Post
	 */
	@GraphQLQuery(name = "recentPosts")
	public List<Post> getRecentPosts(
			@GraphQLArgument(name = "offset") final Integer offset,
			@GraphQLArgument(name = "count") final Integer count) {

		log.debug("Get recent Posts offset: " + offset + ", count: " + count);

		return getPostRepo().getRecents(offset, count);
	}

}
