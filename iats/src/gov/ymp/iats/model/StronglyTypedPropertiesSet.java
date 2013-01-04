package gov.ymp.iats.model;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public abstract class StronglyTypedPropertiesSet
{
	protected StronglyTypedPropertiesSet(String resourceName)
        throws IOException, IncompletePropertyFileException
	{
        _props = new Properties();
        InputStream propsIn = getClass().getResourceAsStream(resourceName);
        if (propsIn != null) {
            try {
                _props.load(propsIn);
            } finally {
                propsIn.close();
            }
        } else {
            throw new IncompletePropertyFileException(
                "Could not find properties file identified by resource name '"
                + resourceName + "'." );
        }
        postLoadTest();
	}

    protected abstract void postLoadTest()
        throws IncompletePropertyFileException;

    protected final String checkNonNull(String value)
    {
        if (value == null) {
            throw new NullPointerException();
        } else {
            return value;
        }
    }

    protected final String getSimpleValue(String key)
    {
        return (String) _props.get(key);
    }

    protected final String getIndexedValue(String key, int index)
    {
        return (String) _props.get(key + index);
    }

    private Properties _props;

    public static class IncompletePropertyFileException extends Exception
    {
        public IncompletePropertyFileException()
        {
            super();
        }

        public IncompletePropertyFileException(String msg)
        {
            super(msg);
        }

        public IncompletePropertyFileException(String msg, Throwable cause)
        {
            super(msg, cause);
        }

        public IncompletePropertyFileException(Throwable cause)
        {
            super(cause);
        }
    };
}
