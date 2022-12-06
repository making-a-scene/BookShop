package shoppingmall.bookshop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 448472121L;

    public static final QUser user = new QUser("user");

    public final shoppingmall.bookshop.QBaseEntity _super = new shoppingmall.bookshop.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath oAuth2Id = createString("oAuth2Id");

    public final StringPath password = createString("password");

    public final EnumPath<shoppingmall.bookshop.authentication.socialLogin.Provider> provider = createEnum("provider", shoppingmall.bookshop.authentication.socialLogin.Provider.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<shoppingmall.bookshop.authentication.Role> role = createEnum("role", shoppingmall.bookshop.authentication.Role.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath userId = createString("userId");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

