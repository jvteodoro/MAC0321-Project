# NotificationHandler

## Introduction

`NotificationHandler` manages notification fetching, WebSocket triggers, and badge count.  
It renders the notification button and the notification list.

## Usage

```javascript
import NotificationHandler from './NotificationList';
```

## Example use

```javascript
const MyPage = props => {
  return (
    <main>
      <NotificationHandler />
    </main>
  );
};
```

## NotificationList (internal)

`NotificationList` is used by `NotificationHandler` and receives:

- `notifications` (array)
- `visible` (bool)
| --------- | -------- | ------------ | ---------- |
| className | string   | -            | -          |
